  Uses: Remoting

    Feature: I can set the pressure range

      #! Remoting use pub, sub
      Scenario: Sending pressure will cause the pressure difference to be calculated
        Given I set the pressure to 200, 300
        Then the pressure difference is 100

      #! Remoting use pub, sub
      Scenario: Pressure deltas are processed atomically
        Given I set the pressure to 600, 700
        And the pressure difference is 100
        When I set the pressure to 800, 1000
        Then the pressure difference is 200
        And the last pressure difference is 100







