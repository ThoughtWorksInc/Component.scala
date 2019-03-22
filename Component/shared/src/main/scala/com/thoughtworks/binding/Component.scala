package com.thoughtworks.binding

import com.thoughtworks.binding.Binding.BindingInstances.monadSyntax._
import com.thoughtworks.binding.Component.ChangeSet

/**
  * @author 杨博 (Yang Bo)
  */
trait Component[-State, +Result] {
  def render(state: Binding[State]): Binding[Result]

  def apply[State0 <: State](state: State0): ChangeSet[Result] = {
    ChangeSet(this, state)
  }
}

object Component {
  sealed trait ChangeSet[+Result] {
    private[Component] type State
    private[Component] val component: Component[State, Result]
    private[Component] val state: State
  }

  private object ChangeSet {
    type Aux[State0, +Result] = ChangeSet[Result] {
      type State = State0
    }
    def apply[State0, Result](component0: Component[State0, Result], state0: State0): ChangeSet.Aux[State0, Result] =
      new ChangeSet[Result] {
        type State = State0
        val component: Component[State, Result] = component0
        val state: State = state0
      }
  }

  def partialUpdate[Result](changeSet: Binding[ChangeSet[Result]]): Binding[Result] = {
    changeSet match {
      case typed: Binding[ChangeSet.Aux[_, Result]] =>
        val stateBinding = typed.map(_.state)
        typed.map(_.component).flatMap(_.render(stateBinding))
    }
  }

}
