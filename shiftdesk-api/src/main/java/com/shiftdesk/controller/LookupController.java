package com.shiftdesk.controller;
import com.shiftdesk.entity.Store;
import com.shiftdesk.repository.StoreRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LookupController {
  private final StoreRepository stores;
  public LookupController(StoreRepository stores) { this.stores = stores; }
  @GetMapping("/stores") public List<Store> stores() { return stores.findAll(); }
}
