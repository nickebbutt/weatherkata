  Uses: Remoting

    Feature: I can travel by Thameslink trains

      Feature-Start:

      #! Remoting use pub, sub
      Scenario: I can commute by Thameslink when it's raining fish and the temperature and wind conditions are ideal
        Given I set temp, wind and precipitation to 18, 0, Fish
        Then I can travel by train

      #! Remoting use pub, sub
      Scenario: Given any other weather conditions the Thameslink trains will not be a good choice
        Given I set temp, wind and precipitation to 19, 0, Fish
        Then I can't travel by train
        Or I set temp, wind and precipitation to 18, 1, Fish
        Then I can't travel by train
        Or I set temp, wind and precipitation to 18, 0, None
        Then I can't travel by train







