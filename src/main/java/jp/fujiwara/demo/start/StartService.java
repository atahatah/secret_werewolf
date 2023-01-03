package jp.fujiwara.demo.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartService {
    @Autowired
    InitialData initialData;
}
