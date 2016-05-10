class Group:
    def __init__(self, name, path):
        self.name = name
        self.path = path
        self.samples = set()

    def add(self, sample):
        self.samples.add(sample)

    def get_path(self):
        return self.path
