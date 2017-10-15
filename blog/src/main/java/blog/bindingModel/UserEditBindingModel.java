package blog.bindingModel;

import java.util.*;

public class UserEditBindingModel extends UserBindingModel{
        private List<Integer> roles;

        public UserEditBindingModel(){
            this.roles=new ArrayList<>();
        }

        public List<Integer> getRoles(){
            return roles;
        }

        public void setRoles(List<Integer> roles){
            this.roles=roles;
        }
}
