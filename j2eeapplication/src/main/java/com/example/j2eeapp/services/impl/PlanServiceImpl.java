package com.example.j2eeapp.services.impl;

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import com.example.j2eeapp.dao.PlanDao;
import com.example.j2eeapp.domain.PlanEntity;
import com.example.j2eeapp.services.PlanService;
import org.primefaces.component.inputtext.InputText;

/**
 * Service providing service methods to work with user data and entity.
 * 
 * @author Arthur Rukshan
 */
public class PlanServiceImpl implements PlanService {

	private PlanDao planDao;

	public boolean createPlan(PlanEntity planEntity) {
		if (!planDao.checkAvailable(planEntity.getPlanName())) {
			FacesMessage message = constructErrorMessage(String.format(getMessageBundle().getString("userExistsMsg"), planEntity.getUserName()), null);
			getFacesContext().addMessage(null, message);

			return false;
		}

		try {
			planDao.save(planEntity);
		} catch(Exception e) {
			FacesMessage message = constructFatalMessage(e.getMessage(), null);
			getFacesContext().addMessage(null, message);

			return false;
		}

		return true;
	}

	public boolean checkAvailable(AjaxBehaviorEvent event) {
		InputText inputText = (InputText) event.getSource();
		String value = (String) inputText.getValue();

		boolean available = planDao.checkAvailable(value);

		if (!available) {
			FacesMessage message = constructErrorMessage(null, String.format(getMessageBundle().getString("userExistsMsg"), value));
			getFacesContext().addMessage(event.getComponent().getClientId(), message);
		} else {
			FacesMessage message = constructInfoMessage(null, String.format(getMessageBundle().getString("userAvailableMsg"), value));
			getFacesContext().addMessage(event.getComponent().getClientId(), message);
		}

		return available;
	}

	public PlanEntity loadPlanEntityByPlanName(String planName) {
		return planDao.loadPlanByPlanName(planName);
	}

	public List<PlanEntity> loadPlanEntities(){
		return planDao.loadPlans();
	}

	protected FacesMessage constructErrorMessage(String message, String detail){
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, message, detail);
	}

	protected FacesMessage constructInfoMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_INFO, message, detail);
	}

	protected FacesMessage constructFatalMessage(String message, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_FATAL, message, detail);
	}

	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	protected ResourceBundle getMessageBundle() {
		return ResourceBundle.getBundle("message-labels");
	}

	public PlanDao getPlanDao() {
		return planDao;
	}

	public void setPlanDao(PlanDao planDao) {
		this.planDao = planDao;
	}
}