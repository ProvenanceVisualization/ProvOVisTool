package Base;

import Action.ImportVisualStyleTask;
import App.CytoVisProject;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

import java.io.File;

public class ImportVisualStyleTaskFactory extends AbstractTaskFactory {

    private CytoVisProject cytoVisProject;
    private ImportVisualStyleTask importVisualStyleTask;

    public ImportVisualStyleTaskFactory(CytoVisProject cytoVisProject){
        this.cytoVisProject = cytoVisProject;
        importVisualStyleTask = (new ImportVisualStyleTask(cytoVisProject));
    }

    public ImportVisualStyleTask getImportVisualStyleTask() {
        return importVisualStyleTask;
    }

    public TaskIterator createTaskIterator() {
        return new TaskIterator(importVisualStyleTask);
    }
}
