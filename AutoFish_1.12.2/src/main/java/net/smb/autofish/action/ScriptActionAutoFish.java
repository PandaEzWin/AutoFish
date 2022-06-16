package net.smb.autofish.action;

import net.eq2online.macros.scripting.api.APIVersion;
import net.eq2online.macros.scripting.api.IMacro;
import net.eq2online.macros.scripting.api.IMacroAction;
import net.eq2online.macros.scripting.api.IReturnValue;
import net.eq2online.macros.scripting.api.IScriptActionProvider;
import net.eq2online.macros.scripting.parser.ScriptAction;
import net.eq2online.macros.scripting.parser.ScriptContext;
import net.smb.autofish.ModuleInfo;
import net.smb.autofish.VariableProviderAutoFish;

@APIVersion(ModuleInfo.API_VERSION)
public class ScriptActionAutoFish extends ScriptAction {
    public ScriptActionAutoFish() {
        super(ScriptContext.MAIN, "autofish");
    }

    public void onInit() {
        this.context.getCore().registerScriptAction(this);
    }

    public IReturnValue execute(IScriptActionProvider provider, IMacro macro, IMacroAction instance, String rawParams, String[] params) {
        String arg = provider.expand(macro, params[0], false);

        if (arg.equals("on") && !VariableProviderAutoFish.autoFishEnabled) {
            VariableProviderAutoFish.autoFishEnabled = true;
        } else if (arg.equals("off") && VariableProviderAutoFish.autoFishEnabled) {
            VariableProviderAutoFish.autoFishEnabled = false;
        }

        return null;
    }
}
