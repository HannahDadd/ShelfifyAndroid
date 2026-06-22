package hannah.bd.shelfify.modals

data class OnboardingPage(
    val description: String
)

val onboardingPages = listOf(
    OnboardingPage(
        description = "Welcome Writer! You've stumbled across an abandoned library!"
    ),
    OnboardingPage(
        description = "With every word you add to your writing projects, the library will grow until one day, you can add your own book to these shelves!"
    ),
    OnboardingPage(
        description = "Use Shelfify's focus mode, daily notifications and motivational widgets to help you write. We don't collect any data and work fully offline so it's totally distraction free."
    ),
    OnboardingPage(
        description = "Let's get started- I can't wait to see what you do with the place."
    )
)