<?hh

<<__DynamicallyCallable>> function handler($name, $obj, inout $args) :mixed{
  echo "----HANDLER----\n";
  var_dump($name, $obj, $args);
  echo "---------------\n";
  throw new Exception;
}

<<__DynamicallyCallable>> function another_handler($name, $obj, inout $args) :mixed{
  echo "----HANDLER----\n";
  var_dump($name, $obj, $args);
  echo "---------------\n";
  throw new Exception;
}

function frap($arg) :mixed{
  echo "frap $arg\n";
}

class Blark {
  public static function sfrap() :mixed{
    echo "static frap\n";
  }
  public function frap() :mixed{
    echo "non-static frap\n";
  }
}
<<__EntryPoint>>
function main_entry(): void {

  // Intercept a function
  fb_intercept2('frap', HH\dynamic_fun('handler'));
  try {
    call_user_func(frap<>, 'callfunc');
  } catch (Exception $e) {
    echo "caught call user func 1\n";
  }

  // Replace with closure
  fb_intercept2('frap', ($_1, $_2, inout $_3) ==> {
    echo "Closure! wooooo\n";
    throw new Exception;
  });
  try {
    call_user_func(frap<>, 'claptrap');
  } catch (Exception $e) {
    echo "caught closure 1\n";
  }

  // Intercept static method
  fb_intercept2('Blark::sfrap', HH\dynamic_fun('handler'));
  try {
    call_user_func(vec['Blark', 'sfrap']);
  } catch (Exception $e) {
    echo "caught static call 1\n";
  }

  // Intercept non-static method
  $b = new Blark();
  fb_intercept2('Blark::frap', HH\dynamic_fun('handler'));
  try {
    call_user_func(vec[$b, 'frap']);
  } catch (Exception $e) {
    echo "caught non-static call 1\n";
  }

  // MULTI-INTERCEPT!
  fb_intercept2('frap', HH\dynamic_fun('handler'));
  fb_intercept2('handler', HH\dynamic_fun('another_handler'));
  try {
    call_user_func(frap<>, 'claptrap');
  } catch (Exception $e) {
    echo "caught double intercept 1\n";
  }
}
