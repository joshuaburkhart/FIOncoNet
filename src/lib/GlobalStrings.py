
class GlobalStrings:

    # Key Names
    group_name1 = 'group_name1'
    group_path1 = 'group_path1'
    group_name2 = 'group_name2'
    group_path2 = 'group_path2'
    group_samples1 = 'group_samples1'
    group_samples2 = 'group_samples2'
    group = 'group'
    name = 'name'
    path = 'path'
    file = 'file'
    samples = 'samples'
    variant_name = 'variant_name'
    locus = 'locus'
    id = 'id'
    chrom = 'chrom'
    pos = 'pos'
    exists = 'exists'
    description = 'description'
    info = 'info'
    group_name = 'group_name'
    sample_name = 'sample_name'

    # Regular Expressions
    variant_file_line_rgx = '(?P<chrom>[0-9XY]+)\s+(?P<pos>[0-9]+)\s+(?P<id>[a-zA-Z0-9_.-]+)\s+(?P<ref>[a-zA-Z0-9_.-]+)\s+(?P<alt>[a-zA-Z0-9_.,-]+)\s+(?P<qual>[0-9]+)\s+(?P<filter>[a-zA-Z0-9_.,:;=-]+)\s+(?P<info>.+)'