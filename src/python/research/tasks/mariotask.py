__author__ = "Sergey Karakovskiy, sergey at idsia fullstop ch; Tom Schaul"
__date__ = "$May 7, 2009 12:47:18 PM$"

from pybrain.rl.tasks.episodic import EpisodicTask
from client.marioenvironment import MarioEnvironment

if __name__ != "__main__":
    print "Loading %s ..." % __name__;

class MarioTask(EpisodicTask):
    """Encapsulates Mario specific options and transfers them to EpisodicTask"""

    def __init__(self, *args, **kwargs):
        EpisodicTask.__init__(self, MarioEnvironment(*args, **kwargs))
        #self.reset()
    
    def reset(self):
        EpisodicTask.reset(self)
        self.finished = False
        self.reward = 0
        self.status = 0        

    def isFinished(self):
        return self.finished

    def getObservation(self):
        obs = EpisodicTask.getObservation(self)
        if len(obs) == 2:
            self.reward = obs[1]
            self.status = obs[0]
            self.finished = True
        return obs
    
    def performAction(self, action):
        if not self.isFinished():
            EpisodicTask.performAction(self, action)            

    def getReward(self):
        """ Fitness gained on the level """
        return self.reward
    
    def getWinStatus(self):
        return self.status