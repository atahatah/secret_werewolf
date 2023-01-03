package jp.fujiwara.demo.global.child;

import org.springframework.stereotype.Service;

import jp.fujiwara.demo.start.StartModel;

@Service
public class ChildDataService {
    final ChildDataModel model = new ChildDataModel();

    public void init(StartModel startModel) {
        model.setParentIpAddress(startModel.getParentIpAddress());
    }
}
