  Uses: Remoting

    Feature: I can travel by snowmobile

      #! Remoting use pub, sub
      Scenario: I can travel by snowmobile when it is freezing
        When I set the temperature to 0
        Then I can travel by snowmobile

      #! Remoting use pub, sub
      Scenario: I can't travel by snowmobile when it is subtropical
        When I set the temperature to 30
        Then I can't travel by snowmobile

      #! Remoting use pub, sub
      Scenario: I can travel by snowmobile when it is polar
        When I set the temperature to -20
        Then I can travel by snowmobile


