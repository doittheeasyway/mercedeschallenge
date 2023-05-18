Feature: Challenge for Mercedes Benz

  @CT1
  Scenario: Validate A Class models price are between £15,000 and £60,000
    Given Hugo is researching is researching for specific models
    When he selects model "Hatchbacks"
    And he does mouse over "A Class" model available
    And then proceeds to "Build your car"
    And filter by Fuel type "Diesel"
    Then he looks up prices are between "15.000" to "60.000"

  @CT2
  Scenario Outline: Validate A Class models price are between £15,000 and £60,000
    Given "<User>" is researching for specific models
    When "<User>" selects model "<Model>"
    And "<User>" does mouse over "<CarClass>" model available
    And then proceeds to "<SiteArea>"
    And filter by Fuel type "<FuelType>"
    Then he looks up prices are between "<MinValue>" to "<MaxValue>"
    Examples:
      | User | Model      | CarClass | SiteArea       | FuelType | MinValue | MaxValue |
      | Hugo | Hatchbacks | A Class  | Build your car | Diesel   | 15.000   | 60.000   |
