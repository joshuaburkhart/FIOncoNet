# Calculate FIOncoNet

import sys
from src.lib.OptionParser import OptionParser
from src.lib.Study import Study
from src.lib.ObjectFactory import ObjectFactory

S = ObjectFactory().build(Study, OptionParser(sys.argv).parse())

# Store Study
S.serialize()

# Write analysis file with Study values
