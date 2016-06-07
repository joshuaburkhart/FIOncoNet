from src.lib.Variant import Variant


class Sample:
    def __init__(self, file, group_name):
        self.file = file
        self.sample_name = file
        self.group = group_name
        self.variants = set()

    def add(self, variant):
        self.variants.add(variant)
