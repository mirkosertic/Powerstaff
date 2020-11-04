package de.powerstaff.web.backingbean;

import de.powerstaff.business.entity.Tag;

public class TagSelectionState {

    private Tag tag;
    private boolean selected;

    public TagSelectionState(final Tag tag, final boolean selected) {
        this.tag = tag;
        this.selected = selected;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(final Tag tag) {
        this.tag = tag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
