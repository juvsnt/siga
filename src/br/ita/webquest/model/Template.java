/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ita.webquest.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="Template")
public class Template implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Template() {
    }

    public Template(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        
        String text = "";
        
        text += "Template id: "+id+"\n";
        text += "name: "+name+"\n";
        
        return text;
    }
}
