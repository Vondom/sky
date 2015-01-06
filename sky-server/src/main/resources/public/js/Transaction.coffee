
class sky.Transaction
  constructor: (@commitCount, @parent, @commitFunction) ->
    @initCommitCount = @commitCount;

    if (!@commitFunction)
      if (_.isFunction(@parent))
        @commitFunction = @parent;
        @parent = null;

  reset: (count) ->
    if (count)
      @initCommitCount = count;
    @commitCount = @initCommitCount;

  commit: () ->
    @commitCount--;
    if (@commitCount <= 0)
      if (@commitFunction)
        @commitFunction();

      if (@parent)
        @parent.commit();
