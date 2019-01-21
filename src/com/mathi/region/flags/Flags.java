package com.mathi.region.flags;

/**
 *
 * @author zMathi
 */
public final class Flags {

    public final static StateFlag PVP = new StateFlag("pvp", StateFlag.State.ALLOW);
    public final static StateFlag BUILD = new StateFlag("construir", StateFlag.State.ALLOW);
    public final static StateFlag MOBS = new StateFlag("mobs", StateFlag.State.ALLOW);
    public final static StateFlag ENTRY = new StateFlag("entrar", StateFlag.State.ALLOW);
    public final static StateFlag EXIT = new StateFlag("sair", StateFlag.State.ALLOW);
    public final static StateFlag DAMAGE = new StateFlag("dano", StateFlag.State.ALLOW);
    public final static SetFlag ALLOWED_COMMANDS = new SetFlag("comandos-permitidos", new CommandFlag(null));
    public final static SetFlag BLOCKED_COMMANDS = new SetFlag("comandos-bloqueados", new CommandFlag(null));
    public final static Flag[] FLAGS = {
        PVP, BUILD, MOBS, ENTRY, EXIT, DAMAGE,
        ALLOWED_COMMANDS, BLOCKED_COMMANDS};
    
    public static Flag<?> getFlag(String flagName){
        for (Flag flag : FLAGS){
            if (flag.getName().equalsIgnoreCase(flagName)){
                return flag;
            }
        }
        return null;
    }
}
