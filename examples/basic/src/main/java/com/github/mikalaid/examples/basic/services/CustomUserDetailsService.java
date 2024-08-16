<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/services/CustomUserDetailsService.java
package com.yosik.examples.basic.services;
========
package com.github.mikalaid.examples.basic.services;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/services/CustomUserDetailsService.java

import java.util.Arrays;
import java.util.List;

<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/services/CustomUserDetailsService.java
import com.yosik.examples.basic.config.user.CustomUserDetails;
import com.yosik.examples.basic.config.user.FeaturesRolloutGroup;
import com.yosik.examples.basic.config.user.Market;
========
import com.github.mikalaid.examples.basic.config.user.CustomUserDetails;
import com.github.mikalaid.examples.basic.config.user.FeaturesRolloutGroup;
import com.github.mikalaid.examples.basic.config.user.Market;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/services/CustomUserDetailsService.java
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsService implements UserDetailsService {

    private List<CustomUserDetails> users = Arrays.asList(
            new CustomUserDetails("euUser", "password1", Market.EU, FeaturesRolloutGroup.PUBLIC_TESTER, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))),
            new CustomUserDetails("usUser", "password2", Market.US, FeaturesRolloutGroup.REGULAR, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}