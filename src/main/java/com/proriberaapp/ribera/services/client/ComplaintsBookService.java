package com.proriberaapp.ribera.services.client;

import com.proriberaapp.ribera.Domain.entities.ComplaintsBookEntity;

public interface ComplaintsBookService {
    ComplaintsBookEntity createComplaint(ComplaintsBookEntity complaint);
}