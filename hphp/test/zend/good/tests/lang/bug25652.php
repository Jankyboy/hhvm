<?hh
<<__DynamicallyCallable>>
function testfunc ($var) :mixed{
    echo "testfunc $var\n";
}

class foo {
    public $arr = vec['testfunc'];
    function bar () :mixed{
        HH\dynamic_fun($this->arr[0])('testvalue');
    }
}
<<__EntryPoint>> function main(): void {
$a = new foo ();
$a->bar ();
}
