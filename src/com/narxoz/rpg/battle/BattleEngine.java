package com.narxoz.rpg.battle;

import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    
    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }
    
    private void executeTurn(List<Combatant> attackers,
                         List<Combatant> defenders,
                         EncounterResult result) {

    for (Combatant attacker : attackers) {

        if (!attacker.isAlive() || defenders.isEmpty())
            continue;

        Combatant target = defenders.get(random.nextInt(defenders.size()));

        int damage = attacker.getAttackPower();
        target.takeDamage(damage);

        result.addLog(attacker.getName()
                + " hits "
                + target.getName()
                + " for "
                + damage);

        if (!target.isAlive()) {
            result.addLog(target.getName() + " was defeated!");
        }
    }
}

    public void reset() {
        // TODO: reset any battle state if you add it
    }

   public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {

    if (teamA == null || teamB == null || teamA.isEmpty() || teamB.isEmpty()) {
        throw new IllegalArgumentException("Teams must not be empty");
    }

    EncounterResult result = new EncounterResult();
    int rounds = 0;

    while (!teamA.isEmpty() && !teamB.isEmpty()) {

        rounds++;
        result.addLog("=== Round " + rounds + " ===");

        executeTurn(teamA, teamB, result);
        executeTurn(teamB, teamA, result);

        teamA.removeIf(c -> !c.isAlive());
        teamB.removeIf(c -> !c.isAlive());
    }

    String winner = teamA.isEmpty() ? "Enemies" : "Heroes";

    result.setWinner(winner);
    result.setRounds(rounds);
    result.addLog("Winner: " + winner);

    return result;
}
}
