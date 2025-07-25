<?hh
// Copyright 2004-present Facebook. All Rights Reserved.

class A {
  public $x = 123;
  <<__LateInit>> public $y;
  public $z = 'abc';
}

<<__DynamicallyCallable>> function test_iter($a) :mixed{
  foreach ($a as $p) {
    echo "$p ";
  }
  echo "\n";
}

<<__DynamicallyCallable>> function test_obj_prop_array($a) :mixed{
  var_dump(HH\object_prop_array($a));
}

function run_test($func) :mixed{
  echo "============= $func ===============\n";

  $a = new A();
  try {
    HH\dynamic_fun($func)($a);
  } catch (Exception $e) {
    echo $e->getMessage() . "\n";
  }

  $a->y = 700;
  try {
    HH\dynamic_fun($func)($a);
  } catch (Exception $e) {
    echo $e->getMessage() . "\n";
  }

  unset($a->y);
  try {
    HH\dynamic_fun($func)($a);
  } catch (Exception $e) {
    echo $e->getMessage() . "\n";
  }
}
<<__EntryPoint>> function main(): void {
run_test('test_iter');
run_test('test_obj_prop_array');
}
