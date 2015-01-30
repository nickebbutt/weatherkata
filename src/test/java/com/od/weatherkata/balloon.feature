  Uses: Remoting

    Feature: I can travel by balloon

      #! Remoting use pub, sub
      Scenario: I can fly by balloon when wind is less than five and precipitation not fish
        Given I set temp, wind and precipitation to 0, 4, None
        Then I can travel by balloon

      #! Remoting use pub, sub
      Scenario: I cannot fly when precipitation is fish
        Given I set temp, wind and precipitation to 0, 4, Fish
        Then I can't travel by balloon

      #! Remoting use pub, sub
      Scenario: I cannot fly when wind is 5 or higher
        Given I set temp, wind and precipitation to 0, 5, None
        Then I can't travel by balloon

      #! Remoting use pub, sub
      Scenario: I can fly in the arctic
        Given I set temp, wind and precipitation to -20, 4, Snow
        Then I can travel by balloon

      #! Remoting use pub, sub
      Scenario: I can fly in the tropics
        Given I set temp, wind and precipitation to 40, 0, Rain
        Then I can travel by balloon



