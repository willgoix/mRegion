package com.mathi.region.flags;

import com.mathi.region.flags.exception.InvalidFlagException;
import com.mathi.region.manager.exception.RegionException;
import org.bukkit.command.CommandSender;

/**
 *
 * @author zMathi
 */
public class StateFlag extends Flag<StateFlag.State> {

    public enum State {

        ALLOW(true), DENY(false);

        public final boolean booleana;

        private State(boolean booleana) {
            this.booleana = booleana;
        }

        public boolean getBoolean() {
            return booleana;
        }

        public static State getState(String string) {
            string = string.toLowerCase().trim();
            if (string.equals("sim") || string.equals("yes") || string.equals("allow") || string.equals("true")) {
                return ALLOW;
            } else if (string.equals("nao") || string.equals("não") || string.equals("no") || string.equals("deny") || string.equals("false")) {
                return DENY;
            }
            throw new InvalidFlagException("'" + string + "' não é um StateFlag.");
        }
    }

    private final State defaultState;

    public StateFlag(String name, State defaultState) {
        super(name);
        this.defaultState = defaultState;
    }

    public State getDefaultState() {
        return defaultState;
    }

    @Override
    public State parseInput(CommandSender sender, String input) throws InvalidFlagException{
        if (input.equalsIgnoreCase("nada") || input.equalsIgnoreCase("null")) {
            return null;
        } else {
            return State.getState(input);
        }
    }

    @Override
    public Object toObject(State value) {
        return value == State.ALLOW ? "allow" : "deny";
    }

    @Override
    public State fromObject(Object object) {
        if (object instanceof String) {
            return State.getState((String) object);
        }
        throw new InvalidFlagException("Object não é uma String.");
    }
}
