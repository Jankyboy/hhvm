(*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the "hack" directory of this source tree.
 *
 *)
open Hh_prelude
module SN = Naming_special_names

module Env = struct
  let in_class t Aast.{ c_name; c_kind; c_final; _ } =
    Naming_phase_env.
      {
        t with
        elab_call = Elab_call.{ current_class = Some (c_name, c_kind, c_final) };
      }
end

let on_expr on_error ((annot, pos, expr_) as expr) ~ctx =
  let (res, errs) =
    match expr_ with
    | Aast.(Call { func = (_, _, Id (_, fn_name)); unpacked_arg; _ } as call)
      when String.equal fn_name SN.PreNamespacedFunctions.echo ->
      let errs =
        Option.to_list
        @@ Option.map
             ~f:(fun (_, pos, _) ->
               Naming_phase_error.naming @@ Naming_error.Too_few_arguments pos)
             unpacked_arg
      in
      (Ok call, errs)
    | Aast.(
        Call { func = (_, fn_expr_pos, Id (_, fn_name)); args; unpacked_arg; _ })
      when String.equal fn_name SN.AutoimportedFunctions.meth_caller ->
      (* TODO[mjt] targs is ignored entirely here - shouldn't we generate
         and error to say they are invalid for this function? *)
      let errs =
        Option.to_list
        @@ Option.map
             ~f:(fun (_, pos, _) ->
               Naming_phase_error.naming @@ Naming_error.Too_few_arguments pos)
             unpacked_arg
      in
      begin
        match args with
        | []
        | [_] ->
          let args_err =
            Naming_phase_error.naming
            @@ Naming_error.Too_few_arguments fn_expr_pos
          in
          (Error fn_expr_pos, args_err :: errs)
        | [Aast_defs.Anormal e1; Aast_defs.Anormal e2] -> begin
          match (e1, e2) with
          | Aast.
              ( (_, _, Class_const ((_, _, CI cl), (_, mem))),
                (_, pm, String meth) )
            when String.equal mem SN.Members.mClass ->
            (Ok (Aast.Method_caller (cl, (pm, meth))), errs)
          | ((_, p, _), _) ->
            let meth_err =
              Naming_phase_error.naming @@ Naming_error.Illegal_meth_caller p
            in
            (Error p, meth_err :: errs)
        end
        | [Aast_defs.Ainout _; _]
        | [_; Aast_defs.Ainout _] ->
          let meth_err =
            Naming_phase_error.naming
            @@ Naming_error.Illegal_meth_caller fn_expr_pos
          in
          (Error fn_expr_pos, meth_err :: errs)
        | _ ->
          let args_err =
            Naming_phase_error.naming
            @@ Naming_error.Too_many_arguments fn_expr_pos
          in
          (Error fn_expr_pos, args_err :: errs)
      end
    | _ -> (Ok expr_, [])
  in
  List.iter ~f:on_error errs;
  match res with
  | Ok expr_ -> (ctx, Ok (annot, pos, expr_))
  | Error _pos -> (ctx, Error (Naming_phase_error.invalid_expr expr))

let on_class_ c ~ctx = (Env.in_class ctx c, Ok c)

let top_down_pass =
  let id = Aast.Pass.identity () in
  Naming_phase_pass.top_down
    Aast.Pass.
      { id with on_ty_class_ = Some (fun elem ~ctx -> on_class_ elem ~ctx) }

let bottom_up_pass on_error =
  let id = Aast.Pass.identity () in
  Naming_phase_pass.bottom_up
    Aast.Pass.
      {
        id with
        on_ty_expr = Some (fun elem ~ctx -> on_expr on_error elem ~ctx);
      }
