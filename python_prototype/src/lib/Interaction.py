class Interaction:
    """
    Represents a protein complex consisting of 2 or more proteins
    """

    def __init__(self, interaction_name, constituent_proteins,description=None):
        self.interaction_name = interaction_name
        self.constituent_proteins = constituent_proteins
        if description is not None: self.description = description

    def group_agreement(self, group_name):
        """
        Calculate the average agreement within the named group
        :param group_name: the name of the group
        :return: the average agreement within the named group
        """
        if not hasattr(self, '_group_agreement'):
            self._group_agreement = sum(
                [x.group_agreement(group_name) for x in self.constituent_proteins]
            ) / len(self.constituent_proteins)
        return self._group_agreement

    def total_agreement(self):
        """
        Calculate the average agreement weighted by group size
        :return: the average agreement weighted by group size
        """
        if not hasattr(self, '_total_agreement'):
            self._total_agreement = sum(
                [x.total_agreement for x in self.constituent_proteins]
            ) / len(self.constituent_proteins)
        return self._total_agreement
