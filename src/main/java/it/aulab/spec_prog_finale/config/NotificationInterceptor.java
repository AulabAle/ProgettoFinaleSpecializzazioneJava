package it.aulab.spec_prog_finale.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import it.aulab.spec_prog_finale.repositories.ArticleRepository;
import it.aulab.spec_prog_finale.repositories.CarreerRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class NotificationInterceptor implements HandlerInterceptor {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CarreerRequestRepository carreerRequestRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            int revisedCount = articleRepository.findByIsAcceptedFalse().size();
            int carrerCount = carreerRequestRepository.findByIsCheckedFalse().size();
            modelAndView.addObject("articlesToBeRevised", revisedCount);
            modelAndView.addObject("carrerRequests", carrerCount);
        }
    }
}
