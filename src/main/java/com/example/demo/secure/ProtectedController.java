package com.example.demo.secure;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/user")
@RequestScoped
public class ProtectedController {

//    @Inject
//    @Claim("custom-value")
//    private ClaimValue<String> custom;

    @GET
    public String getJWTBasedValue() {//TODO: DAS IST NOCH NICHT PROTECTED!
        return "Protected Resource; Custom value : FOOBAR";
    }
}
