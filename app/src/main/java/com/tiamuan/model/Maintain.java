package com.tiamuan.model;

import java.util.Set;

public class Maintain extends Model {

    private Repair repair;
    private Set<Engineer> engineers;
    private Set<Status> statuses;

    public Maintain() {
    }

//    public Maintain(long id) {
//        super(id);
//    }

    public void addEngineer(Engineer engineer) {
        engineers.add(engineer);
    }


    public void addStatus(Status status) {
        statuses.add(status);
    }

    public Repair getRepair() {
        return repair;
    }

    public void setRepair(Repair repair) {
        this.repair = repair;
    }

    public Set<Engineer> getEngineers() {
        return engineers;
    }

    public void setEngineers(Set<Engineer> engineers) {
        this.engineers = engineers;
    }

    public Set<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<Status> statuses) {
        this.statuses = statuses;
    }
}
