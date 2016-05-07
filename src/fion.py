# Calculate FIOncoNet

"""
---------
S = Study(op(options))
For vf in vfr(S)
    If vfv.valid(vf)
        G = Group(vf)
        For dl in vfp(vfr).data_lines
            M = Sample(dl.arg1 to dl.argN)
            G.add(M)
        S.add(G)
    Else
        Error

"""