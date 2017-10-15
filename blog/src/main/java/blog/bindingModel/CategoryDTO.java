package blog.bindingModel;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by user on 6.10.2017 г..
 */
public class CategoryDTO {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private String name;

    public CategoryDTO(Integer id,String name){
        this.id=id;
        this.name=name;
    }
    public CategoryDTO(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
