
class Study:
    def __init__(self):
        self.groups = set()

    def add(self,group):
        self.groups.add(group)

    def groups(self):
        return list(self.groups).sort()

    def adj_mt(self):
        if not hasattr(self,'_adj_mt'):
            self._adj_mt = self.calculate_adj_mt()
        return self._adj_mt

    def calculate_adj_mt(self):
        """
        figure this out later
        :return: and adjacency matrix representing the graph
        """
        pass

    def group_genes_above_t(self,group_name,t):
        pass

    def total_genes_above_t(self,t):
        pass

    def intersection_genes_above_t(self,t):
        pass

    def group_genes_t_dist(self,group_name):
        pass

    def genes_t_dist(self):
        pass

    def group_proteins_above_t(self,group_name,t):
        pass

    def total_proteins_above_t(self,t):
        pass

    def intersection_proteins_above_t(self,t):
        pass

    def group_proteins_t_dist(self,group_name):
        pass

    def proteins_t_dist(self):
        pass

    def group_interactions_above_t(self,group_name,t):
        pass

    def total_interactions_above_t(self,t):
        pass

    def intersection_interactions_above_t(self,t):
        pass

    def group_interactions_t_dist(self,group_name):
        pass

    def interactions_t_dist(self):
        pass

    def serialize(outfile=None):
        x
        pass