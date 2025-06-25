public enum GameState {
    /** Reset all variables before starting a new game. */
    RESET,
    /** Homescreen with button to play the game. */
    HOME,
    /** Screen to choose your character. */
    CHOOSE_CHARACTER,
    /** Initial tunnel scene when you wake up. */
    TUNNEL,
    /** Dialogue with the portal. */
    PORTAL_DIALOGUE,
    /** Portal cutscene animation. */
    PORTAL_CUTSCENE,
    /** Prefight state where the fight button appears. */
    PREFIGHT,
    /** Heaven stage where movement is possible. */
    HEAVEN,
    /** Interaction stage with Zeus. */
    ZEUS_INTERACTION,
    /** Fight cutscene right before the boss fight. */
    FIGHT_CUTSCENE,
    /** Actual boss fight with Zeus. */
    BOSS_FIGHT,
    /** Death animation after losing the fight. */
    DEATH_ANIMATION,
    /** Lose screen with option to play again. */
    LOSE_SCREEN,
    /** Animation played when the special attack wins the fight. */
    WIN_ANIMATION,
    /** Final win screen with play again option. */
    WIN_SCREEN
}
