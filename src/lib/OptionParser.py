from src.lib.GlobalStrings import GlobalStrings as g

class OptionParser:
    def __init__(self,command_line_args):
        self.command_line_args = command_line_args

    def parse(self):
        parameters = dict()
        parameters[g.group_name1] = self.command_line_args.split(' ')[0]
        parameters[g.group_path1] = parameters[g.group_name1]
        parameters[g.group_name2] = self.command_line_args.split(' ')[1]
        parameters[g.group_path2] = parameters[g.group_name2]
        return parameters