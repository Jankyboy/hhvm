# pyre-strict
import re

from . import base  # usort: skip (must be first, needed for sys.path side-effects)


class WalkStkCommandTestCase(base.TestHHVMBinary):
    def file(self) -> str:
        return "slow/reified-generics/reified-parent.php"

    def test_walkstk_command(self) -> None:
        # Just check we have a stack of the correct form; the specific
        # number of frames, line numbers, etc. may vary.
        self.run_until_breakpoint("checkClassReifiedGenericMismatch")
        _, output = self.run_commands(["walkstk"])
        frames = output.split("\n")
        prog = re.compile(r"#\d+\s+0x[a-f0-9]+\s@\s0x[a-f0-9]+:.*")
        for i, f in enumerate(filter(len, frames)):
            with self.subTest(name=str(i)):
                self.assertRegex(f, prog)
