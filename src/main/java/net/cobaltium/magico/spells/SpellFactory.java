package net.cobaltium.magico.spells;

import com.google.inject.Inject;
import org.slf4j.Logger;

import java.util.Optional;


public class SpellFactory {

    @Inject
    private Logger logger;

    public Optional<Spell> getSpell(String spellClass) {
        try {
            Spell s = (Spell) Class.forName(spellClass).newInstance();
            return Optional.of(s);
        } catch (ClassNotFoundException ex) {
            return Optional.of(new Fireball()); //TODO: replace with a sane default option
        } catch (InstantiationException ex) {
            logger.error("Error creating spell instance: " + ex);
        } catch (IllegalAccessException ex) {
            logger.error(ex.toString());
        }
        return Optional.empty();
    }
}
