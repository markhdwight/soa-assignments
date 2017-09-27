package DataAccessObjectsMaven.com.cooksys;

import java.util.HashSet;
import java.util.Set;

import com.cooksys.dao.PersonDao;
import com.cooksys.entity.Interest;
import com.cooksys.entity.Location;
import com.cooksys.entity.Person;

public class App 
{
    public static void main( String[] args )
    {
        PersonDao pdao = new PersonDao();
        
        System.out.println("These people are in the same interest groups:");
        System.out.println(pdao.findInterestGroups());
        
        Set<Interest> interests = new HashSet<Interest>();
        interests.add(new Interest("anarchist"));
        Person rob = new Person("Tyler","Durden",new Location("Detroit","Michigan","US"),interests);
        pdao.save(rob);
        
        System.out.println("Here's everyone in the table:");
        System.out.println(pdao.getEveryone());
        
        pdao.closeConnection();
    }
}
