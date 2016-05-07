# Calculate FIOncoNet

import sys
from src.lib.OptionParser import OptionParser
from src.lib.Study import Study
from src.lib.Sample import Sample

S = Study(OptionParser(sys.argv))


for vf in vfr([S.G1,S.G2]):
    if vfv.valid(vf):
        M = Sample(vf)
        for dl in vfp(vfr).data_lines:
            M.add(dl.arg1 to dl.argN)
        S.Gx.add(M)
    else:
        raise Exception('Invalid Variant File: {0}'.format(vfv.status(vf)))

#Write analysis file with Study values