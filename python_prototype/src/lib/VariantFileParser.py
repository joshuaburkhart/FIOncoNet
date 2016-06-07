from src.lib.GlobalStrings import GlobalStrings as g
import re


class VariantFileParser:
    @staticmethod
    def data_lines(variant_filename):
        lines = set()
        in_fptr = open(variant_filename)
        while 1:
            line = in_fptr.readline()
            if not line:
                break
            match = re.match(g.variant_file_line_rgx, line)
            if match:
                lines.add({g.variant_name: match.group(g.id),
                           g.locus: '{0}:{1}'.format(match.group(g.chrom),
                                                     match.group(g.pos)),
                           g.exists: True,
                           g.description: match.group(g.info)})
        in_fptr.close()
        return lines
