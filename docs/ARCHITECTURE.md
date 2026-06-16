          ┌────────────┐
          │    AI      │
          └─────┬──────┘
                │ Action
                v
        ┌──────────────┐
        │  GameLoop    │
        └─────┬────────┘
              │ dispatch
┌───────────┼────────────┬────────────┐
v           v            v            v
Movement   Combat     Inventory     Level
System     System      System       System
└────────────┴────────────┴────────────┘
                │
                v
            World State