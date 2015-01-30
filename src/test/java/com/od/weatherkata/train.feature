  Uses: Remoting

    Feature: I can't travel by Thameslink trains

      #! Remoting use pub, sub
      Scenario: I can commute by Thameslink when ideal temp with no wind and raining fish
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







