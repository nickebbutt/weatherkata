  Uses: Remoting
  Uses: Processes
  Uses: Timers

  Feature: I can travel by snowmobile

    #! Processes start pub, sub
    #! Remoting use pub, sub
    Feature-Start:


    ### Show the temperature wind and precipitation values on the UI

    Scenario: The temperature wind and precipitation are set
      Given I set temp, wind and precipitation to 10, 5, Rain
      Then the temperature is 10
      And the wind strength is 5
      And the precipitation is Rain


    ### Snow Mobile

    Scenario: I can travel by snowmobile when it is freezing
      When I set the temperature to 0
      Then I can travel by snowmobile

    Scenario: I can't travel by snowmobile when it is subtropical
      When I set the temperature to 30
      Then I can't travel by snowmobile

    Scenario: I can travel by snowmobile when it is polar
      When I set the temperature to -20
      Then I can travel by snowmobile



    ### Balloon

    Scenario: I can fly by balloon when wind is less than five and precipitation not fish
      Given I set temp, wind and precipitation to 0, 4, None
      Then I can travel by balloon

    Scenario: I cannot fly when precipitation is fish
      Given I set temp, wind and precipitation to 0, 4, Fish
      Then I can't travel by balloon

    Scenario: I cannot fly when wind is 5 or higher
      Given I set temp, wind and precipitation to 0, 5, None
      Then I can't travel by balloon

    Scenario: I can fly in the arctic
      Given I set temp, wind and precipitation to -20, 4, Snow
      Then I can travel by balloon

    Scenario: I can fly in the tropics
      Given I set temp, wind and precipitation to 40, 0, Rain
      Then I can travel by balloon



    ### Train

    Scenario: I can commute by Thameslink when temperature ideal with no wind and raining fish
      Given I set temp, wind and precipitation to 18, 0, Fish
      Then I can travel by train

    Scenario: Thameslink trains do not run in all other circumstances
      Given I set temp, wind and precipitation to 19, 0, Fish
      Then I can't travel by train
      Or I set temp, wind and precipitation to 18, 1, Fish
      Then I can't travel by train
      Or I set temp, wind and precipitation to 18, 0, None
      Then I can't travel by train



    ### Pressure

    Scenario: Sending pressure will cause the pressure difference to be calculated
      Given I set the pressure to 200, 300
      Then the pressure difference is 100

    Scenario: Pressure deltas are processed atomically
      Given I set the pressure to 600, 700
      And the pressure difference is 100
      When I set the pressure to 800, 1000
      Then the pressure difference is 200
      And the last pressure difference is 100
