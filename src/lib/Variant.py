class Variant:
    """
    Represents a nucleotide variant
    """

    def __init__(self, variant_name, locus, group_name, sample_name, exists=True, description=None):
        self.variant_name = variant_name
        self.locus = locus
        self.samples = {group_name: {sample_name: exists}}
        if description is not None: self.description = description

    def add_sample(self, group_name, sample_name, exists=True):
        self.samples[group_name][sample_name] = exists

    def group_agreement(self, group_name):
        """
        Calculate the average agreement within the named group
        :param group_name: the name of the group
        :return: the average agreement within the named group
        """
        return sum(
            sum([self.samples[group_name][sample_name]
             for sample_name
             in self.samples[group_name].keys()])
        ) / len(self.samples[group_name])

    def total_agreement(self):
        """
        Calculate the average agreement weighted by group size
        :return: the average agreement weighted by group size
        """
        return sum([sum([int(self.samples[group_name][sample_name])
                 for sample_name
                 in self.samples[group_name].keys()])
                * (len(self.samples[group_name])
                       / len(self.samples))
                for group_name
                in self.samples.keys()]) / (sum(len(group_name)) for group_name in self.samples)
