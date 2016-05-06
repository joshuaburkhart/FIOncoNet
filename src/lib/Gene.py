class Gene:
    """
    Represents a gene containing 0 or more variants
    """
    def __init__(self,gene_name,gene_uniprot,variants,description=None):
        self.gene_name = gene_name
        self.gene_uniprot = gene_uniprot
        self.variants = variants
        if description is not None: self.description = description

    def group_agreement(self,group_name):
        """
        Calculate the average agreement within the named group
        :param group_name: the name of the group
        :return: the average agreement within the named group
        """
        if not hasattr(self,'_group_agreement'):
            self._group_agreement = sum(
                [x.group_agreement(group_name) for x in self.variants]
                )/len(self.variants)
        return self._group_agreement

    def total_agreement(self):
        """
        Calculate the average agreement weighted by group size
        :return: the average agreement weighted by group size
        """
        if not hasattr(self,'_total_agreement'):
            self._total_agreement = sum(
                [x.total_agreement for x in self.variants]
            ) / len(self.variants)
        return self._total_agreement