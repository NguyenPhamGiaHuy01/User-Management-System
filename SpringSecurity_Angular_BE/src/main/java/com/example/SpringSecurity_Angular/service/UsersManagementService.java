package com.example.SpringSecurity_Angular.service;

import com.example.SpringSecurity_Angular.dto.ReqRes;
import com.example.SpringSecurity_Angular.entity.OurUsers;
import com.example.SpringSecurity_Angular.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ReqRes register(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();

        try {
            OurUsers ourUsers = new OurUsers();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setCity(registrationRequest.getCity());
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setName(registrationRequest.getName());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers ourUsersResult = usersRepo.save(ourUsers);
            if (ourUsersResult.getId()>0) {
                resp.setOurUsers(ourUsersResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

        }catch (Exception e){
            resp.setStatusCode((500));
            resp.setError(e.getMessage());
        }
        return resp;
    }
    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = usersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully Logged In");

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes reqRes = new ReqRes();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            OurUsers users = usersRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                reqRes.setStatusCode(200);
                reqRes.setToken(jwt);

            }
        }catch (Exception e){
            reqRes.setStatusCode((500));
            reqRes.setError(e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getAllUsers(){
        ReqRes reqRes = new ReqRes();

        try{
            List<OurUsers> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            }else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return  reqRes;
        }catch (Exception e){
            reqRes.setStatusCode((500));
            reqRes.setError("Error occurred" + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id){
        ReqRes reqRes = new ReqRes();
        try{
            OurUsers usersById = usersRepo.findById(id).orElseThrow(()-> new RuntimeException("User Not Found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id "+id+"found successfully");
        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: "+e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId){
        ReqRes reqRes = new ReqRes();

        try{
            Optional<OurUsers> usersOptional = usersRepo.findById(userId);
            if (usersOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            }else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: "+e.getMessage());
        }
        return reqRes;
    }


    public ReqRes updateUser(Integer userId, OurUsers updatedUser){
        ReqRes reqRes = new ReqRes();
        try{
            Optional<OurUsers> usersOptional = usersRepo.findById(userId);
            if (usersOptional.isPresent()) {
                OurUsers existingUser = usersOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                if (updatedUser.getPassword() !=null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = usersRepo.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            }
        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: "+e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try{
            Optional<OurUsers> usersOptional = usersRepo.findByEmail(email);
            if (usersOptional.isPresent()) {
                reqRes.setOurUsers(usersOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            }else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user: "+e.getMessage());
        }
        return reqRes;
    }
}
