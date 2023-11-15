package com.xxl.job.executor.domain;

import java.util.Objects;

public class RsTag {
    private static final long serialVersionUID = 266123528399950116L;

    private Long id;
    /**
     * 标签名
     */
    private String tagName;

    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RsTag rsTag = (RsTag) o;
        return Objects.equals(id, rsTag.id) && Objects.equals(tagName, rsTag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagName);
    }

    @Override
    public String toString() {
        return "RsTag{" +
                "id=" + id +
                ", tagName='" + tagName + '\'' +
                '}';
    }
}