package mscs.hms.controller;

import mscs.hms.model.Landlord;
import mscs.hms.service.LandlordService;
import mscs.hms.dto.selectors.LegalEntitySelectorDTO;
import mscs.hms.dto.selectors.PropertySelectorDTO;
import mscs.hms.service.PropertyService;
import mscs.hms.service.LegalEntityService;
import mscs.hms.controller.editors.PropertyEditor;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandlordController extends AbsEntityController<Landlord> {
    
    @Autowired
    private LandlordService landlordService;

    @Autowired
    private LegalEntityService legalEntityService;

    @Autowired
    private PropertyService propertyService;

    @InitBinder
    public void customizeBinding (WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "properties",
                                    new PropertyEditor(propertyService, true));        
    }

    @GetMapping("/landlords")
    public ModelAndView showCompanies(Model model,
                                      @RequestParam("page") Optional<Integer> page,
                                      @RequestParam("size") Optional<Integer> size,
                                      @RequestParam("search") Optional<String> search) {
        LOG.info("In Landlords view");
        int currentPage = page.orElse(DEFAULT_PAGE_NUMBER);
        currentPage = currentPage > 0 ? currentPage - 1 : 0;
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        String searchString = search.orElse(null);
        return getListEntitiesModelView(landlordService.getAll(searchString, currentPage, pageSize));
    }    

    @GetMapping("/landlord_new")
    public ModelAndView newLandlordForm() {
        LOG.info("In landlords new");
        return getEditViewModel(new Landlord(), "new");
    }    

    @GetMapping("/landlord_edit/{id}")
    public ModelAndView editLandlordForm(@PathVariable(value="id") final Integer landlordId) {
        LOG.info("In landlords edit");
        return getEditViewModel(landlordService.getById(landlordId), "edit");        
    }

    @PostMapping("/landlord/delete") 
    public ModelAndView requestOTP( @RequestParam(value="id") Integer id) {
        LOG.info("In landlords delete");
        landlordService.deleteById(id);
        return getListEntitiesModelView(landlordService.getAll(null, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/landlord/edit")
    public ModelAndView processEdit(Landlord landlord) {
        LOG.info("In landlords edit");
        try{
            landlordService.save(landlord);
        }
        catch(Exception ex){
            return getEditViewModel(landlord, getObjectErrorList(ex), "edit");
        }
        return getListEntitiesModelView(landlordService.getAll(null, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
    }

    @PostMapping("/landlord/new")
    public ModelAndView processNew(Landlord landlord) {
        LOG.info("In landlords new");
        try{
            landlordService.save(landlord);
        }
        catch(Exception ex){
            return getEditViewModel(landlord, getObjectErrorList(ex), "edit");
        }
        return getListEntitiesModelView(landlordService.getAll(null, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
    } 
    
    @Override
    public Class<?> getClassType(){
        return Landlord.class;
    }
    @Override
    public String getEditViewPath(){
        return "/landlord_edit";
    }
    @Override
    public String getListViewPath(){
        return "/landlord_list";
    }
    @Override
    public String getNewViewPath(){
        return "/landlord_new";
    }
    @Override
    public String getCrudPath(){
        return "/landlord";
    }
    @Override
    public String getListPath() { return "/landlords";}
    @Override
    public Dictionary<String, List<?>> getSelectLists(){
        Dictionary<String, List<?>> dictionary = new Hashtable<>();
        //Note used same attributeName "systemUser"
        dictionary.put("properties", propertyService.getProperties().stream().map(PropertySelectorDTO::new).collect(Collectors.toList()));
        dictionary.put("legalEntity", legalEntityService.findAll().stream().map(LegalEntitySelectorDTO::new).collect(Collectors.toList()));
        return dictionary;
    }
}