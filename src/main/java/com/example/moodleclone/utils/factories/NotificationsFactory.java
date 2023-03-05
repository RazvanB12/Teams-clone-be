package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.NotificationDTO;
import com.example.moodleclone.entities.Notifications;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class NotificationsFactory {
    private final ModelMapper mapper;

    public List<GenericDTO> mapNotificationsToGenericDTO(List<Notifications> notifications) {
        List<GenericDTO> notificationsDTO = new ArrayList<>();
        notifications.forEach( notification -> {
                    NotificationDTO NotificationDTO = mapNotificationToNotificationDTO(notification);
                    notificationsDTO.add(NotificationDTO);
                }
        );
        return notificationsDTO;
    }

    private NotificationDTO mapNotificationToNotificationDTO(Notifications notification) {
        return mapper.map(notification, NotificationDTO.class);
    }
}
