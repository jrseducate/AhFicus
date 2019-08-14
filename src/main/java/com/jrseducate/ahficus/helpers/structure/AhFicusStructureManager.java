package com.jrseducate.ahficus.helpers.structure;

import java.util.HashMap;
import java.util.Map;

public class AhFicusStructureManager
{
    Map<String, AhFicusStructureHelper> structureHelpers;
    
    public AhFicusStructureManager()
    {
        this.structureHelpers = new HashMap<String, AhFicusStructureHelper>();
    }
    
    public void init()
    {
        addStructureHelper(new StructureHelperRitualSite());
    }
    
    public void addStructureHelper(AhFicusStructureHelper structureHelper)
    {
        this.structureHelpers.put(structureHelper.getName(), structureHelper);
    }
    
    public AhFicusStructureHelper getStructureHelper(String name)
    {
        return this.structureHelpers.get(name);
    }
}
