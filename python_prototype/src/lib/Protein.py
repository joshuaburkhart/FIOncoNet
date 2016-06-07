class Protein:
    """
    Represents a protein
    """

    def __init__(self, protein_name, source_gene,description=None):
        self.protein_name = protein_name
        self.source_gene = source_gene
        if description is not None: self.description = description

    def group_agreement(self, group_name):
        """
        Calculate the agreement of the named group
        :param group_name: the name of the group
        :return: the agreement within the named group
        """
        if not hasattr(self, '_group_agreement'):
            self._group_agreement = self.source_gene.group_agreement(group_name)
        return self._group_agreement

    def total_agreement(self):
        """
        Calculate the average agreement weighted by group size
        :return: the average agreement weighted by group size
        """
        if not hasattr(self, '_total_agreement'):
            self._total_agreement = self.source_gene.total_agreement
        return self._total_agreement
