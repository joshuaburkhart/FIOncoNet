from src.lib.Study import Study
from src.lib.Sample import Sample
from src.lib.Group import Group
from src.lib.Variant import Variant
from src.lib.GlobalStrings import GlobalStrings as g
from src.lib.VariantFileResolver import VariantFileResolver
from src.lib.VariantFileValidator import VariantFileValidator
from src.lib.VariantFileParser import VariantFileParser


class ObjectFactory:

    @staticmethod
    def build(type, parameters):
        if type is Study:
            return ObjectFactory.build_study(parameters)
        elif type is Sample:
            return ObjectFactory.build_sample(parameters)
        elif type is Group:
            return ObjectFactory.build_group(parameters)
        elif type is Variant:
            return ObjectFactory.build_variant(parameters)
        else:
            return None

    @staticmethod
    def build_study(parameters):
        S = Study()
        S.add(ObjectFactory.build(Group, {g.name: parameters[g.group_name1],
                                 g.path: parameters[g.group_path1],
                                 g.samples: parameters[g.group_samples1]}))
        S.add(ObjectFactory.build(Group, {g.name: parameters[g.group_name2],
                                 g.path: parameters[g.group_path2],
                                 g.samples: parameters[g.group_samples2]}))
        for group in S.groups:
            for file in VariantFileResolver.files(group.get_path()):
                if VariantFileValidator.valid(file):
                    ObjectFactory.build(Sample, {g.file: file, g.group: group})
                else:
                    raise Exception('Invalid Variant File: {0}'.format(VariantFileValidator.status(file)))

    @staticmethod
    def build_sample(parameters):
        variant_names = dict()
        M = Sample(parameters[g.file],parameters[g.group])
        for dl in VariantFileParser.data_lines(parameters[g.file]):
            if dl[g.variant_name] not in variant_names.keys():
                variant = ObjectFactory.build(Variant, {g.variant_name: dl[g.variant_name],
                                          g.locus: dl[g.locus],
                                          g.group: M.group,
                                          g.sample_name: M.sample_name,
                                          g.exists: dl[g.exists],
                                          g.description: dl[g.description]})
                variant_names[g.variant_name] = variant
            else:
                variant = variant_names[dl[g.variant_name]]
            M.add(variant)
        parameters[g.group].add(M)
        return M

    @staticmethod
    def build_group(parameters):
        G = Group(parameters[g.name], parameters[g.path])
        [G.add(sample) for sample in parameters[g.samples]]
        return G

    @staticmethod
    def build_variant(parameters):
        V = Variant(parameters[g.variant_name], parameters[g.locus],
                    parameters[g.exists], parameters[g.description])
        return V