package com.example.projectApp.service;
import com.example.projectApp.Dao.GenericDao;
import com.example.projectApp.Dto.TypeDto;
import com.example.projectApp.model.Type;
import com.example.projectApp.repository.TypeRepository;
import com.example.projectApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TypeService {
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository, UserRepository userRepository) {
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
    }
    public TypeDto getById(Long typeId, Boolean details) {
        return typeRepository.findByIdAndUserIdOrUserId(typeId, UserService.getCurrentLoggedInUser().getId(), 1L).map(type -> new TypeDto(type, details)).orElse(null);
    }

    //Retrieves a group by its name.
    public TypeDto getByName(String name, Boolean details) {
        return typeRepository.findByNameAndUserIdOrUserId(name, UserService.getCurrentLoggedInUser().getId(), 1L).map(type -> new TypeDto(type, details)).orElse(null);
    }
    public List<TypeDto> getAll(Boolean details) {
        return typeRepository.findAllByUserIdOrUserId(UserService.getCurrentLoggedInUser().getId(), 1L).stream().map(type -> new TypeDto(type, details)).toList();
    }
// create new type by manager
    public GenericDao<TypeDto> createType(TypeDto dto) {
        GenericDao<TypeDto> returnDao = new GenericDao<>();
        List<String> errors = new ArrayList<>();

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Type name cannot be empty");
        }


        if (errors.isEmpty()) {
            Long uid = UserService.getCurrentLoggedInUser().getId();
            Optional<Type> retrievedType = typeRepository.findByNameAndUserId(dto.getName(), uid);

            if (retrievedType.isEmpty()) {
                Type type = new Type(dto);
                type.setUser(userRepository.findById(uid).get());
                type = typeRepository.save(type);
                returnDao.setObject(new TypeDto(type, false));
            } else {
                errors.add("Type already exists");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }

// edit project type by manager
    public GenericDao<TypeDto> editType(TypeDto dto) {
        GenericDao<TypeDto> returnDao = new GenericDao<>();

        List<String> errors = new ArrayList<>();

        if (dto.getId() == null) {
            errors.add("Type ID cannot be empty");
        }

        if (dto.getName() == null || dto.getName().isBlank()) {
            errors.add("Type name cannot be empty");
        }

        if (errors.isEmpty()) {
            Optional<Type> retrievedType = typeRepository.findByIdAndUserId(dto.getId(), UserService.getCurrentLoggedInUser().getId());

            if (retrievedType.isPresent()) {
                if (retrievedType.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {

                    retrievedType.get().setName(dto.getName());
                    Type savedType = typeRepository.save(retrievedType.get());

                    returnDao.setObject(new TypeDto(savedType, false));
                } else {
                    errors.add("Cannot edit type of another user");
                }
            } else {
                errors.add("Type does not exist");
            }
        }

        if (!errors.isEmpty()) {
            returnDao.setErrors(errors);
        }

        return returnDao;
    }
    // Deletes a Type if the current user is the owner and returns success or failure.
    public GenericDao<Boolean> deleteType(Long TypeId) {
        List<String> errors = new ArrayList<>();
        // Retrieve the Type based on user and TypeId
        Optional<Type> retrievedType = typeRepository.findByIdAndUserId(TypeId, UserService.getCurrentLoggedInUser().getId());
        if (retrievedType.isPresent()) {
            if (retrievedType.get().getUser().getId().equals(UserService.getCurrentLoggedInUser().getId())) {
                typeRepository.deleteById(TypeId);
            } else {
                errors.add("Cannot delete type of another user");
            }
            return errors.isEmpty() ?
                    new GenericDao<>(true, errors) :
                    new GenericDao<>(false, errors);
        } else {
            errors.add("Type does not exist");
            return new GenericDao<>(false, errors);
        }
    }
}